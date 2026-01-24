package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.TriggerScryEffectsAction;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbsV1.LymeanGene;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.orbsV1.*;

public class ChannelMagic
        extends BaseEvoCard {
    public static final String cardID = "ChannelMagic";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SeerSear.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 2;
    private static final int UPGRADE_FATE_AMT = 1;
    private static final int LYMEAN_FATE_AMT = 1;

    public ChannelMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        LavafolkGene lavafolkGene = new LavafolkGene();
        addToBot(new ChannelAction(lavafolkGene));
        if (isPlayerInThisForm(LymeanGene.ID)) {
            fate += LYMEAN_FATE_AMT;
        }
        addToBot(new ChannelMagicAction(fate, lavafolkGene));
        formEffect(LymeanGene.ID);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene.ID)) {
            this.glowColor = LymeanGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public static class ChannelMagicAction extends AbstractGameAction {

        private static final UIStrings uiStrings;
        public static final String[] TEXT;
        static {
            uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
            TEXT = uiStrings.TEXT;
        }

        private float startingDuration;
        private CardGroup fateGroup;
        private LavafolkGene lavafolkGene;

        public ChannelMagicAction(int fateAmount, LavafolkGene lavafolkGene) {
            this.amount = fateAmount;
            this.lavafolkGene = lavafolkGene;

            this.actionType = ActionType.CARD_MANIPULATION;
            this.startingDuration = Settings.ACTION_DUR_FAST;
            this.duration = this.startingDuration;
        }

        public void update() {
            // copied from ScryAction
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
            } else {
//			Iterator var1;
                if (this.duration == this.startingDuration) {
                    addToTop(new TriggerScryEffectsAction());
//				AbstractDungeon.player.powers.forEach(AbstractPower::onScry);
//				var1 = AbstractDungeon.player.powers.iterator();
//				while(var1.hasNext()) {
//					AbstractPower p = (AbstractPower)var1.next();
//					p.onScry();
//				}

                    if (AbstractDungeon.player.drawPile.isEmpty()
                            || amount <= 0) {
                        this.isDone = true;
                        return;
                    }
                    fateGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    CardGroup drawPile = AbstractDungeon.player.drawPile;
                    for (int i = Math.min(amount, drawPile.size()); i > 0; --i) {
                        AbstractCard card = drawPile.getRandomCard(true);
                        fateGroup.addToTop(card);
                        drawPile.removeCard(card);
                    }
                    fateGroup.group.forEach(drawPile::addToTop);
                    if (fateGroup.isEmpty()) {
                        this.isDone = true;
                        return;
                    }

                    AbstractDungeon.gridSelectScreen.open(fateGroup, fateGroup.size(), true, TEXT[0]);
                } else {
                    if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                        AbstractDungeon.gridSelectScreen.selectedCards.forEach(c -> {
                            AbstractDungeon.player.drawPile.removeCard(c);
                            AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                            lavafolkGene.onEvoke();
                        });
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    }
                }

                this.tickDuration();
            }
        }
    }
}