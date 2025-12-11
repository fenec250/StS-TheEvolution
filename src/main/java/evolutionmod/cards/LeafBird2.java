package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.GrowthPower;

import java.util.Iterator;

public class LeafBird2 extends BaseEvoCard {
    public static final String ID = "evolutionmod:LeafBird";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LeafBird.png";
    private static final int COST = 1;
    private static final int DISCARD_AMT = 3;
    private static final int UPGRADE_GROWTH_AMT = 1;
    private static final int HARPY_DRAW_AMT = 2;

    public LeafBird2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DISCARD_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        formEffect(HarpyGene.ID, ()-> addToBot(new DrawCardAction(HARPY_DRAW_AMT)));
        addToBot(new LeafBird2Action(p, this.magicNumber));
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, UPGRADE_GROWTH_AMT)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LeafBird2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_DISCARD_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		if (isPlayerInThisForm(HarpyGene.ID)) {
			this.glowColor = HarpyGene.COLOR;
		} else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
	}


    public static class LeafBird2Action extends AbstractGameAction {

        private int discardAmount;
        private String message;

        public LeafBird2Action(AbstractCreature player, int discardAmount) {
            this.setValues(player, player);
            this.discardAmount = discardAmount;
            this.duration = this.startDuration = 0.5f;
            this.actionType = ActionType.SPECIAL;
            this.message = EXTENDED_DESCRIPTION[0] + discardAmount + EXTENDED_DESCRIPTION[1];
        }

        @Override
        public void update() {
            if (this.duration == this.startDuration) {
                AbstractDungeon.handCardSelectScreen.open(this.message, this.discardAmount, true, true);
                this.addToBot(new WaitAction(0.25F));
                this.tickDuration();
            } else {
                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                        int size = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
                        this.addToTop(new ApplyPowerAction(this.target, this.source, new GrowthPower(this.target, size)));
                        Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

                        while(var1.hasNext()) {
                            AbstractCard c = (AbstractCard)var1.next();
                            AbstractDungeon.player.hand.moveToDiscardPile(c);
                            GameActionManager.incrementDiscard(false);
                            c.triggerOnManualDiscard();
                        }
                    }
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                }
                this.tickDuration();
            }
        }
    }
}