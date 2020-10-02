package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;
import evolutionmod.powers.PoisonCoatedPower;

public class PoisonSpit
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PoisonSpit";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantForm.png";
    private static final int COST = 0;
    private static final int ENVENOM_AMT = 2;
    private static final int UPGRADE_ENVENOM_AMT = 1;

    public PoisonSpit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ENVENOM_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int poison = this.magicNumber;
        if (!upgraded && AbstractGene.isPlayerInThisForm(LizardGene.ID)) {
            poison += UPGRADE_ENVENOM_AMT;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new PoisonCoatedPower(p, poison)));

        if (upgraded || !AbstractGene.isPlayerInThisForm(LizardGene.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LizardGene()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PoisonSpit();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ENVENOM_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            initializeDescription();
//            this.target = CardTarget.ALL_ENEMY;
        }
    }
}