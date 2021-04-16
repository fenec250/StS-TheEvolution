package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class Pheromones
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Pheromones";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Pheromones.png";
    private static final int COST = 0;
    private static final int FORM_VULNERABLE_AMT = 2;
    private static final int UPGRADE_VULNERABLE_AMT = 1;

    public Pheromones() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.magicNumber = this.baseMagicNumber = FORM_VULNERABLE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int vulnerable = (upgraded? UPGRADE_VULNERABLE_AMT : 0);
        if (isPlayerInTheseForms(PlantGene.ID, SuccubusGene.ID)) {
            vulnerable += this.magicNumber;
        }
        formEffect(PlantGene.ID, () -> formEffect(SuccubusGene.ID));

        final int finalVulnerable = vulnerable;
        if (finalVulnerable > 0) {
            AbstractDungeon.getMonsters().monsters.stream()
                    .filter(mo -> !mo.isDeadOrEscaped())
                    .forEach(mo -> {
                        addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, finalVulnerable, false)));
//                        addToBot(new ApplyPowerAction(mo, p, new LustPower(mo, this.magicNumber)));
//                        addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -this.magicNumber), -this.magicNumber));
//                        if (!mo.hasPower("Artifact")) {
//                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, this.magicNumber), this.magicNumber));
//                        }
                    });
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pheromones();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_REDUCTION_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 2;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return true;
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        switch (glowIndex) {
            case 0:
                return isPlayerInThisForm(PlantGene.ID) ? PlantGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            case 1:
                return isPlayerInThisForm(SuccubusGene.ID, PlantGene.ID) ? SuccubusGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            default:
                return BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}