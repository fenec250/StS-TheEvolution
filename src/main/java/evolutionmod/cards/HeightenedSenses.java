package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RagePower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.Iterator;

public class HeightenedSenses
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:HeightenedSenses";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/HeightenedSenses.png";
    private static final int COST = 1;
    private static final int HARPY_DRAW_AMT = 3;
    private static final int UPGRADED_DRAW_AMT = 1;
    private static final int BEAST_RAGE_AMT = 2;
    private static final int UPGRADE_RAGE_AMT = 1;

    public HeightenedSenses() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = HARPY_DRAW_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        AbstractDungeon.actionManager.addToBottom(new HeightenedSensesAction(p, this.magicNumber));
        formEffect(HarpyGene.ID, () ->
            addToBot(new DrawCardAction(this.magicNumber, new FollowUpAction()))
        );
        formEffect(BeastGene.ID, () -> {
            int rage = this.upgraded ? BEAST_RAGE_AMT + UPGRADE_RAGE_AMT : BEAST_RAGE_AMT;
            addToBot(new ApplyPowerAction(p, p, new RagePower(p, rage)));
        });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_DRAW_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public static class FollowUpAction extends AbstractGameAction {
        public FollowUpAction() {
            this.duration = 0.001F;
        }

        public void update() {
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
            this.tickDuration();
            if (this.isDone) {
                Iterator var1 = DrawCardAction.drawnCards.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    if (c.type != AbstractCard.CardType.ATTACK) {
                        AbstractDungeon.player.hand.moveToDiscardPile(c);
                        c.triggerOnManualDiscard();
                        GameActionManager.incrementDiscard(false);
                    }
                }
            }

        }
    }
}