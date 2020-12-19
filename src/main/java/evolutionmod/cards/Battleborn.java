package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.patches.AbstractCardEnum;

public class Battleborn
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Battleborn";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurPower.png";
    private static final int COST = 1;
    private static final int STRENGTH_AMT = 1;
    private static final int UPGRADE_STRENGTH_AMT = 1;
    private static final int FORMS_STRENGTH_AMT = 1;

    public Battleborn() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STRENGTH_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new StrengthPower(p, this.magicNumber)));
        formEffect(CentaurGene.ID, () -> {
            if (!this.upgraded) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                        new StrengthPower(p, FORMS_STRENGTH_AMT)));
            } else {
                formEffect(BeastGene.ID, () ->
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                                new StrengthPower(p, FORMS_STRENGTH_AMT))));
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Battleborn();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(CentaurGene.ID) && (!upgraded || isPlayerInThisForm(BeastGene.ID))) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}